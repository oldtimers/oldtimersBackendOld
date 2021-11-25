package pl.pazurkiewicz.oldtimers_rally;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@Transactional
class NPlusOneQueriesLoggingTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void nPlusOneQueriesDetection_isLoggingWhenDetectingNPlusOneQueries() {
        // Fetch the messages without the authors
        User user = userRepository.getById(5);

        // Trigger N+1 queries
        List<Integer> names = user.getUserGroups().stream()
                .map(userGroup -> userGroup.getEvent().getId())
                .collect(Collectors.toList());
    }
}
