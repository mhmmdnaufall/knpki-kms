package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.repository.QuoteRepository;
import org.knpkid.kms.service.QuoteService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuoteServiceImpl implements QuoteService {

    private final ValidationService validationService;

    private final QuoteRepository quoteRepository;

    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public QuoteResponse create(CreateQuoteRequest request, Admin admin) {
        validationService.validate(request);

        final var author = authorRepository.findByName(request.author())
                .orElseGet(() -> {
                    final var newAuthor = new Author();
                    newAuthor.setName(request.author());
                    return authorRepository.save(newAuthor);
                });

        final var quote = new Quote();
        quote.setBody(request.body());
        quote.setAuthor(author);
        quote.setAdmin(admin);

        return toQuoteResponse(quoteRepository.save(quote));
    }

    @Transactional
    @Override
    public void delete(Integer quoteId, Admin admin) {
        final var quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "article with id '" + quoteId + "' is not found")
                );
        checkQuoteAdmin(quote, admin);

        quoteRepository.delete(quote);
        log.info("quote with id '{}', has been deleted", quote.getId());
    }

    private QuoteResponse toQuoteResponse(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getBody(),
                quote.getAuthor(),
                quote.getAdmin()
        );
    }

    private void checkQuoteAdmin(Quote quote, Admin admin) {
        if (quote.getAdmin().equals(admin)) return;
        log.warn("'{}' tries to modify '{}' article", admin.getUsername(), quote.getAdmin().getUsername());
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this article belongs to someone else");
    }

}
