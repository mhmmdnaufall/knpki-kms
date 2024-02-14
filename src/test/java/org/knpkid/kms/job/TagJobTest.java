package org.knpkid.kms.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.repository.TagRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagJobTest {
    
    @InjectMocks
    private TagJob tagJob;
    
    @Mock
    private TagRepository tagRepository;

    @Test
    void removeAbandonedTags() {

        when(tagRepository.findByArticlesEmpty()).thenReturn(List.of(new Tag()));
        doNothing().when(tagRepository).deleteAll(any());

        tagJob.removeAbandonedTag();

        verify(tagRepository).findByArticlesEmpty();
        verify(tagRepository).deleteAll(any());

    }

    @Test
    void removeAbandonedTags_emptyTags() {

        when(tagRepository.findByArticlesEmpty()).thenReturn(Collections.emptyList());

        tagJob.removeAbandonedTag();

        verify(tagRepository).findByArticlesEmpty();
        verify(tagRepository, times(0)).deleteAll(any());

    }

}