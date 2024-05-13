package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.*;
import org.knpkid.kms.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping(
            path = "/api/quotes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<QuoteResponse> create(@RequestBody CreateQuoteRequest request, Admin admin) {
        final var quoteResponse = quoteService.create(request, admin);
        return new WebResponse<>(quoteResponse, null, null);
    }

    @DeleteMapping("/api/quotes/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer quoteId, Admin admin) {
        quoteService.delete(quoteId, admin);
    }

    @PutMapping(
            path = "/api/quotes/{quoteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<QuoteResponse> update(@PathVariable Integer quoteId,
                                             @RequestBody UpdateQuoteRequest request,
                                             Admin admin) {
        final var quoteResponse = quoteService.update(quoteId, request, admin);
        return new WebResponse<>(quoteResponse, null, null);
    }

    @GetMapping(
            path = "/api/quotes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<QuoteResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "12") Integer size) {

        final var quoteResponsePage = quoteService.getAll(page, size);
        return new WebResponse<>(
                quoteResponsePage.getContent(), null,
                new PagingResponse(
                        quoteResponsePage.getNumber(),
                        quoteResponsePage.getTotalPages(),
                        quoteResponsePage.getSize()
                ));
    }

    @GetMapping(
            path = "/api/quotes/{quoteId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<QuoteResponse> get(@PathVariable Integer quoteId) {
        final var quoteResponse = quoteService.get(quoteId);
        return new WebResponse<>(quoteResponse, null, null);
    }

}
