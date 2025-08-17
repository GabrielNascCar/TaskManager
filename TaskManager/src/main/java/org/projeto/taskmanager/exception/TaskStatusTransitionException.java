package org.projeto.taskmanager.exception;

public class TaskStatusTransitionException extends RuntimeException {
  public TaskStatusTransitionException(String currentStatus, String newStatus) {
    super("Invalid status transition from " + currentStatus + " to " + newStatus);
  }
}
