package com.fieldflow.shared.exception;

public final class InvalidEvidenceFileException extends RuntimeException {

	public enum Reason {
		EMPTY,
		TOO_LARGE,
		UNSUPPORTED_TYPE
	}

	private final Reason reason;

	public InvalidEvidenceFileException(Reason reason, String message) {
		super(message);
		this.reason = reason;
	}

	public Reason getReason() {
		return reason;
	}
}
