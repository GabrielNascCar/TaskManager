package org.projeto.taskmanager.exception;

public class TaskValidationException extends RuntimeException {
  public TaskValidationException(String field, String message) {
    super("Invalid task data - " + field + ": " + message);
  }
}
