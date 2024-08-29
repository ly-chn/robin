package kim.nzxy.robin.spring.boot.aop;

import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.annotations.RobinTopicCollector;
import kim.nzxy.robin.daily.RobinGetUp;
import kim.nzxy.robin.spring.boot.interceptor.RobinMetadataRootObject;
import kim.nzxy.robin.util.RobinUtil;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ly-chn
 */
@EnableAspectJAutoProxy
@Aspect
public class CustomMetadataAspect implements BeanFactoryAware {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
    private final TemplateParserContext parserContext = new TemplateParserContext();
    @Setter
    private BeanFactory beanFactory;

    private String parse(String expression, MethodBasedEvaluationContext context) {
        if (RobinUtil.isEmpty(expression)) {
            return expression;
        }
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));

        return parser.parseExpression(expression, parserContext).getValue(context, String.class);
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
        Method targetMethod = signature.getMethod();

        Map<String, String> topicMetadataMap = new HashMap<>(8);

        // 初始化
        RobinMetadataRootObject rootObject = new RobinMetadataRootObject(targetMethod, extractArgs(targetMethod, args),
                joinPoint.getTarget(), joinPoint.getTarget().getClass());
        // 赋值
        MethodBasedEvaluationContext context =
                new MethodBasedEvaluationContext(rootObject, targetMethod, args, pnd);

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

    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        } else {
            Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
            Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
            System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
            System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
            return combinedArgs;
        }
    }
}
