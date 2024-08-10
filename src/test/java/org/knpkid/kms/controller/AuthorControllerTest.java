package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.model.AuthorResponse;
import org.knpkid.kms.service.AuthorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {


    @InjectMocks
    private AuthorController authorController;

    @Mock
    private AuthorService authorService;

    @Test
    void get() {

        var authorResponse = new AuthorResponse(
                1, "author", Collections.emptyList(), Collections.emptyList()
        );

        when(authorService.get(1)).thenReturn(authorResponse);

        var webResponseAuthorResponse = authorController.get(1);

        verify(authorService).get(1);
        assertEquals(authorResponse, webResponseAuthorResponse.data());
        assertNull(webResponseAuthorResponse.errors());
        assertNull(webResponseAuthorResponse.paging());

    }
}