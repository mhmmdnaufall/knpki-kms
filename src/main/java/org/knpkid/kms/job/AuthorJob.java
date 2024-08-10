package org.knpkid.kms.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorJob {

    private final AuthorRepository authorRepository;

    @Scheduled(cron = "0 5 0 * * *") // every 00:05
    @Transactional
    public void removeAbandonedAuthor() {
        log.info("checking for abandoned author . . .");
        var authors = authorRepository.findByArticlesEmptyAndQuotesEmpty();
        log.info("total abandoned authors : {}", authors.size());

        if (authors.isEmpty()) return;

        // if authors != empty
        authorRepository.deleteAll(authors);

        var deletedAuthorsId = authors.stream().map(Author::getId).toList();
        log.info("author with id {} have been removed", deletedAuthorsId);

    }

}
