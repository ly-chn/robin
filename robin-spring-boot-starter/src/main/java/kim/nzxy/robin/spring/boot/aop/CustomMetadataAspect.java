package kim.nzxy.robin.spring.boot.aop;

import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.annotations.RobinTopicCollector;
import kim.nzxy.robin.daily.RobinGetUp;
import kim.nzxy.robin.util.RobinUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ly-chn
 */
@EnableAspectJAutoProxy
@Aspect
public class CustomMetadataAspect {
    /**
     * SpEL Parser
     */
    public static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    private static String parse(String expression, EvaluationContext context) {
        if (RobinUtil.isEmpty(expression)) {
            return expression;
        }
        return EXPRESSION_PARSER.parseExpression(expression).getValue(context, String.class);
    }

    @Pointcut("@within(kim.nzxy.robin.annotations.RobinTopic) || @annotation(kim.nzxy.robin.annotations.RobinTopic)")
    private void single() {
    }

    @Pointcut("@within(kim.nzxy.robin.annotations.RobinTopicCollector) || @annotation(kim.nzxy.robin.annotations.RobinTopicCollector)")
    private void multiple() {
    }

    /**
     * AOP拦截
     */
    @Before("single() || multiple()")
    public void atBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        Method targetMethod = signature.getMethod();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Map<String, String> topicMetadataMap = new HashMap<>(8);
        AnnotatedElementUtils.getMergedRepeatableAnnotations(targetMethod.getDeclaringClass(),
                        RobinTopic.class,
                        RobinTopicCollector.class)
                .forEach(it -> topicMetadataMap.put(it.value(), parse(it.metadata(), context)));
        AnnotatedElementUtils.getMergedRepeatableAnnotations(targetMethod,
                        RobinTopic.class,
                        RobinTopicCollector.class)
                .forEach(it -> topicMetadataMap.put(it.value(), parse(it.metadata(), context)));
        RobinGetUp.hunger(topicMetadataMap);
    }
}
