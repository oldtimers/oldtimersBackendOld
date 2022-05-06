package pl.pazurkiewicz.oldtimers_rally.service;

import net.bytebuddy.utility.RandomString;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidNumberOfCrews;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;
import pl.pazurkiewicz.oldtimers_rally.model.StageEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.QrCodeListWrapper;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.QrCodeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class QrCodeService {
    private final CrewService crewService;
    private final CrewRepository crewRepository;
    private final QrCodeRepository qrCodeRepository;
    private final MyConfigurationProperties configurationProperties;
    private final EventRepository eventRepository;

    public QrCodeService(CrewService crewService, CrewRepository crewRepository, QrCodeRepository qrCodeRepository, MyConfigurationProperties configurationProperties, EventRepository eventRepository) {
        this.crewService = crewService;
        this.crewRepository = crewRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.configurationProperties = configurationProperties;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public byte[] generateQrList(Event event) throws com.lowagie.text.DocumentException, IOException {
//        crewService.assignAllCrewsToNumber(event);
        List<QrCode> qrs = generateMissingQr(event);

//        assignAllUsersToQrCode(event);
        return generatePDF(event, qrs);
    }

    @Transactional
    protected List<QrCode> generateMissingQr(Event event) {
        int max = event.getMaxCrewNumber() + 1;
        List<QrCode> eventsQr = qrCodeRepository.getByEventOrderByNumberAsc(event);
        List<QrCode> results = new LinkedList<>();
        int li = 0; // list iterator
        int i = 1; // current number
        while (i < max) {
            int n = eventsQr.size() > li ? eventsQr.get(li).getNumber() : Integer.MAX_VALUE;
            while (i < Integer.min(max, n)) {
                results.add(qrCodeRepository.save(generateNewQrCode(event, i++)));
            }
            if (n < max) {
                results.add(eventsQr.get(li++));
                i = n + 1;
            }
        }
        if (event.getStage() == StageEnum.NEW) {
            event.setStage(StageEnum.PRESENTS);
        }
        eventRepository.save(event);
        return results;
    }

    private byte[] generatePDF(Event event, List<QrCode> qrs) throws IOException, com.lowagie.text.DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        String x = parseThymeleafTemplate(event, qrs);
        renderer.setDocumentFromString(x);
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    private String parseThymeleafTemplate(Event event, List<QrCode> qrs) throws IOException {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        QrCodeListWrapper qrCodes = generateQrCodeWrapper(event, qrs);
        context.setVariable("wrapper", qrCodes);
        return templateEngine.process("templates/qr/template.html", context);
    }

    private QrCodeListWrapper generateQrCodeWrapper(Event event, List<QrCode> qrs) throws IOException {
        List<QrCodeListWrapper.QrCodePiece> qrCodePieces = new LinkedList<>();
//        List<Crew> crews = crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(event.getUrl());
        if (qrs == null) {
            qrs = qrCodeRepository.getByEventOrderByNumberAsc(event);
        }
        for (QrCode qr : qrs) {
            qrCodePieces.add(new QrCodeListWrapper.QrCodePiece(qr.getNumber(), generateQrBase64(qr.getQr())));
        }
        return new QrCodeListWrapper(qrCodePieces);
    }

    private String generateQrBase64(String qr) throws IOException {
        ByteArrayOutputStream stream = QRCode
                .from(qr)
                .withSize(250, 250)
                .stream();
        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
        BufferedImage image = ImageIO.read(bis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        return new String(Base64.getEncoder().encode(out.toByteArray()));
    }

    @Transactional
    public void assignUsersToQrCode(Event event) throws InvalidNumberOfCrews {
        List<QrCode> emptyQrCodes = qrCodeRepository.getByEventAndCrewIsNullOrderByNumberAsc(event);
        List<Crew> crewsToFill = crewRepository.getSortedPresentEmptyCrews(event);
        if (crewsToFill.size() > emptyQrCodes.size()) {
            throw new InvalidNumberOfCrews(String.format("Empty QR: %d, it is required: %d", emptyQrCodes.size(), crewsToFill.size()));
        }
        int iQr = 0;
        for (Crew crew : crewsToFill) {
            crew.setQrCode(emptyQrCodes.get(iQr++));
        }
        if (event.getStage() == StageEnum.PRESENTS) {
            event.setStage(StageEnum.NUMBERS);
            eventRepository.save(event);
        }
    }


    private QrCode generateNewQrCode(Event event, int number) {
        QrCode qrCode = new QrCode();
        qrCode.setEvent(event);
        qrCode.setQr(String.format("%s?qr=%s", configurationProperties.getRealUrl(), RandomString.make(40)));
        qrCode.setNumber(number);
        return qrCode;
    }


}
