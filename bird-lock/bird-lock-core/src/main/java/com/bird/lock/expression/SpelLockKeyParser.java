package com.bird.lock.expression;

import com.bird.core.session.SessionContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Parameter;

/**
 * Spel表达式解析
 *
 * 模版："...{#param}..."
 *
 * @author liuxx
 * @since 2020/12/2
 */
public class SpelLockKeyParser implements ILockKeyParser {

    private final static String SESSION_KEY = "session";
    private final TemplateParserContext templateParserContext = new TemplateParserContext("{", "}");

    @Override
    public String parse(String keyPattern, Parameter[] parameters, Object[] args) {
        if (StringUtils.isBlank(keyPattern) || ArrayUtils.isEmpty(parameters) || ArrayUtils.isEmpty(args)) {
            return keyPattern;
        }

        ExpressionParser expressionParser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

        //默认添加当前用户的Session变量
        evaluationContext.setVariable(SESSION_KEY, SessionContext.getSession());

        //添加方法中的参数变量
        int n = Integer.min(parameters.length, args.length);
        for (int i = 0; i < n; i++) {
            evaluationContext.setVariable(parameters[i].getName(), args[i]);
        }
        return expressionParser.parseExpression(keyPattern, templateParserContext).getValue(evaluationContext, String.class);
    }
}