package org.knpkid.kms.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.repository.TagRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TagJob {

    public final TagRepository tagRepository;

    @Scheduled(cron = "0 0 0 * * *") // every 00:00
    @Transactional
    public void removeAbandonedTag() {

        log.info("checking for abandoned tags . . .");

        final var tags = tagRepository.findByArticlesEmpty();

        log.info("total abandoned tags : {}", tags.size());

        if (!tags.isEmpty()) {
            tagRepository.deleteAll(tags);

            final var deletedTagId = tags.stream().map(Tag::getId).toList();
            log.info("tags with id {} have been removed", deletedTagId);
        }
    }

}
