package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping(
            path = "/api/quotes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<QuoteResponse> create(@RequestBody CreateQuoteRequest request, Admin admin) {
        final var quoteResponse = quoteService.create(request, admin);
        return new WebResponse<>(quoteResponse, null, null);
    }

    @DeleteMapping("/api/quotes/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer quoteId, Admin admin) {
        quoteService.delete(quoteId, admin);
    }

}
