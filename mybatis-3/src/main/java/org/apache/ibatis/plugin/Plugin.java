/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.plugin;

import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Clinton Begin
 * <p>
 * {@link Intercepts}
 */
public class Plugin implements InvocationHandler {


    // 通过注解定义 {@link Intercepts}
    // 目标对象，即Executor、ParameterHandler、ResultSetHandler、StatementHandler对象
    private final Object target;
    // Intercepts注解指定的方法
    private final Map<Class<?>, Set<Method>> signatureMap;

    // 用户自定义拦截器实例
    private final Interceptor interceptor;

    private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    /**
     * 该方法用于创建Executor、ParameterHandler、ResultSetHandler、StatementHandler的代理对象
     *
     * @param target      被拦截的对象
     * @param interceptor 拦截类
     * @return
     */
    public static Object wrap(Object target, Interceptor interceptor) {
        // 调用getSignatureMap（）方法获取自定义插件中，通过Intercepts注解指定的方法
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        // JDK代理
        if (interfaces.length > 0) {
            return Proxy.newProxyInstance(
                    type.getClassLoader(),
                    interfaces,
                    new Plugin(target, interceptor, signatureMap));
        }
        return target;
    }


    // 执行代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            // 如果该方法是Intercepts注解指定的方法，则调用拦截器实例的intercept（）方法执行拦截逻辑
            Set<Method> methods = signatureMap.get(method.getDeclaringClass());
            // 判断方法是否被拦截
            if (methods != null && methods.contains(method)) {
                // 执行拦截
                return interceptor.intercept(new Invocation(target, method, args));
            }
            // 执行原来的方法
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }

    // 拦截的对象
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        // 获取Intercepts注解信息
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        if (interceptsAnnotation == null) {
            throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }
        // 获取所有Signature注解信息
        Signature[] sigs = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
        // 对所有Signature注解进行遍历，把Signature注解指定拦截的组件及方法添加到Map中
        for (Signature sig : sigs) {
            Set<Method> methods = signatureMap.get(sig.type());
            if (methods == null) {
                methods = new HashSet<Method>();
                signatureMap.put(sig.type(), methods);
            }
            try {
                // 通过methodName + methosArgs获取Method对象
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
            }
        }
        return signatureMap;
    }

    /**
     * 获取目标类型的接口信息
     *
     * @param type
     * @param signatureMap
     * @return
     */
    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

}
