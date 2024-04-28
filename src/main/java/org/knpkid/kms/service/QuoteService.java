package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;

public interface QuoteService {

    QuoteResponse create(CreateQuoteRequest request, Admin admin);

    void delete(Integer quoteId, Admin admin);
}
