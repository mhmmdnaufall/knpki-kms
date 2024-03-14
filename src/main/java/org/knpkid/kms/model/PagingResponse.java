package org.knpkid.kms.model;

import java.io.Serializable;

public record PagingResponse(int currentPage, int totalPage, int size) implements Serializable { }
