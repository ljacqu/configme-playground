package beantest;

import ch.jalu.configme.beanmapper.ExportName;

import java.time.LocalDateTime;

public record GradeRecord(String subject, @ExportName("score") int grade, LocalDateTime issuedAt) {

}
