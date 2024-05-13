package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.model.UpdateQuoteRequest;
import org.springframework.data.domain.Page;

public interface QuoteService {

    QuoteResponse create(CreateQuoteRequest request, Admin admin);

    void delete(Integer quoteId, Admin admin);

    QuoteResponse update(Integer quoteId, UpdateQuoteRequest request, Admin admin);

    Page<QuoteResponse> getAll(int page, int size);
}
