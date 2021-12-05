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
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;
import pl.pazurkiewicz.oldtimers_rally.model.StageEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.QrCodeListWrapper;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.QrCodeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class QrCodeService {
    private final CrewService crewService;
    private final CrewRepository crewRepository;
    private final QrCodeRepository qrCodeRepository;
    private final MyConfigurationProperties configurationProperties;

    public QrCodeService(CrewService crewService, CrewRepository crewRepository, QrCodeRepository qrCodeRepository, MyConfigurationProperties configurationProperties) {
        this.crewService = crewService;
        this.crewRepository = crewRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.configurationProperties = configurationProperties;
    }

    @Transactional
    public byte[] generateFullQrUserList(Event event) throws com.lowagie.text.DocumentException, IOException {
        crewService.assignAllCrewsToNumber(event);
        assignAllUsersToQrCode(event);
        return generatePDF(event);
    }

    private byte[] generatePDF(Event event) throws IOException, com.lowagie.text.DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        String x = parseThymeleafTemplate(event);
        renderer.setDocumentFromString(x);
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    private String parseThymeleafTemplate(Event event) throws IOException {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        QrCodeListWrapper qrCodes = generateQrCodeWrapper(event);
        context.setVariable("wrapper", qrCodes);
        return templateEngine.process("templates/qr/template.html", context);
    }

    private QrCodeListWrapper generateQrCodeWrapper(Event event) throws IOException {
        List<QrCodeListWrapper.QrCodePiece> qrCodePieces = new LinkedList<>();
        List<Crew> crews = crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(event.getUrl());
        for (Crew crew : crews) {
            qrCodePieces.add(new QrCodeListWrapper.QrCodePiece(crew, generateQrBase64(crew.getQrCodes().stream().findAny().get().getQr())));
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
    public void assignAllUsersToQrCode(Event event) {
        Set<Crew> crews = crewRepository.getAllByEventAndQrIsNull(event);
        Set<QrCode> qrCodes = qrCodeRepository.getByEventAndCrewIsNull(event);
        Set<QrCode> result = new HashSet<>();
        for (Crew crew : crews) {
            QrCode qrCode;
            if (qrCodes.isEmpty()) {
                qrCode = generateNewQrCode(event);
            } else {
                qrCode = qrCodes.stream().findAny().get();
                qrCodes.remove(qrCode);
            }
            qrCode.setCrew(crew);
            result.add(qrCode);
        }
        if (event.getStage() == StageEnum.NEW) {
            event.setStage(StageEnum.NUMBERS);
        }
        qrCodeRepository.saveAllAndFlush(result);
    }


    public QrCode generateNewQrCode(Event event) {
        QrCode qrCode = new QrCode();
        qrCode.setEvent(event);
        qrCode.setQr(String.format("%s?qr=%s", configurationProperties.getRealUrl(), RandomString.make(40)));
        return qrCode;
    }


}
