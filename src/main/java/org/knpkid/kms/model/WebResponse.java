package org.knpkid.kms.model;

import java.io.Serializable;

public record WebResponse<T>(T data, String errors, PagingResponse paging) implements Serializable { }
