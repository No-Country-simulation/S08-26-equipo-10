package com.fieldflow.shared.exception;

public final class EvidenceStorageException extends RuntimeException {

	public EvidenceStorageException(String message) {
		super(message);
	}

	public EvidenceStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
