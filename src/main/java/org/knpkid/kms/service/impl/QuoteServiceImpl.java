package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.repository.QuoteRepository;
import org.knpkid.kms.service.QuoteService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuoteServiceImpl implements QuoteService {

    private final ValidationService validationService;

    private final QuoteRepository quoteRepository;

    private final AuthorRepository authorRepository;

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

    private QuoteResponse toQuoteResponse(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getBody(),
                quote.getAuthor(),
                quote.getAdmin()
        );
    }

}
