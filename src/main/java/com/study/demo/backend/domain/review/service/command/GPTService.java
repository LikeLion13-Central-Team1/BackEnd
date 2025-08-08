package com.study.demo.backend.domain.review.service.command;

import java.util.List;

public interface GPTService {

    String summarizeFromContents(List<String> rawContents);
}
