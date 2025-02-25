package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.model.UpdateQuoteRequest;
import org.knpkid.kms.repository.QuoteRepository;
import org.knpkid.kms.service.AuthorService;
import org.knpkid.kms.service.QuoteService;
import org.knpkid.kms.service.ValidationService;
import org.knpkid.kms.utility.ConvertToModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.knpkid.kms.Constant.UPDATED_AT;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    private final AuthorService authorService;

    private final ValidationService validationService;

    @Transactional
    @Override
    public QuoteResponse create(CreateQuoteRequest request, Admin admin) {
        validationService.validate(request);

        var author = authorService.getOrCreateByName(request.author());

        var quote = new Quote();
        quote.setBody(request.body());
        quote.setAuthor(author);
        quote.setAdmin(admin);

        return ConvertToModel.quoteResponse(quoteRepository.save(quote));
    }

    @Transactional
    @Override
    public void delete(Integer quoteId, Admin admin) {
        var quote = getQuoteById(quoteId);
        checkQuoteAdmin(quote, admin);

        quoteRepository.delete(quote);
        log.info("quote with id '{}', has been deleted", quote.getId());
    }

    @Transactional
    @Override
    public QuoteResponse update(Integer quoteId, UpdateQuoteRequest request, Admin admin) {
        validationService.validate(request);

        var quote = getQuoteById(quoteId);
        checkQuoteAdmin(quote, admin);

        var author = authorService.getOrCreateByName(request.author());

        quote.setAuthor(author);
        quote.setBody(request.body());
        quote.setUpdatedAt(LocalDateTime.now());

        return ConvertToModel.quoteResponse(quoteRepository.save(quote));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<QuoteResponse> getAll(int page, int size) {
        return quoteRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT))))
                .map(ConvertToModel::quoteResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public QuoteResponse get(int quoteId) {
        return ConvertToModel.quoteResponse(getQuoteById(quoteId));
    }

    private Quote getQuoteById(Integer quoteId) {
        return quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "quote with id '" + quoteId + "' is not found")
                );
    }

    private void checkQuoteAdmin(Quote quote, Admin admin) {
        if (quote.getAdmin().equals(admin)) return;
        log.warn("'{}' tries to modify '{}' article", admin.getUsername(), quote.getAdmin().getUsername());
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this quote belongs to someone else");
    }

}
