package org.knpkid.kms.model;

public record WebResponse<T>(T data, Object errors, PagingResponse paging) { }
