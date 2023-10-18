package com.keven1z.core.hook.http;

import com.keven1z.core.EngineController;
import com.keven1z.core.hook.http.body.HttpBodyAdviceAdapter;
import com.keven1z.core.hook.http.body.HttpBodyReadAdviceAdapter;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.PolicyUtils;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;

/**
 * 自定义的ClassVisitor，用于访问类的方法
 */
public class HttpClassVisitor extends ClassVisitor {
    private final String className;
    //监控进入hook点，但未成功匹配方法描述，弹出警告告知method或者desc错误
    private boolean isVisitMethod;
    private final Logger hookLogger = Logger.getLogger("hook.info");
    /**
     * 策略名称:http body 读取标志hook点
     */
    private final static String HTTP_BODY = "http_body";
    /**
     * 策略名称:http 生命周期提取hook点
     */
    private final static String HTTP_CIRCLE = "http_circle";
    /**
     * 策略名称:http body读取hook点
     */
    private final static String HTTP_BODY_READ = "http_body_read";

    public HttpClassVisitor(ClassVisitor classVisitor, String className) {
        super(Opcodes.ASM9, classVisitor);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        //如果是本地方法，不进行hook
        if (isNative(access)) {
            return methodVisitor;
        }
        Policy policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getHttp());
        if (policy == null) {
            //打印错误日志
            return methodVisitor;
        }
        String policyName = policy.getName();
        JSRInlinerAdapter jsrInlinerAdapter = new JSRInlinerAdapter(methodVisitor, access, name, descriptor, signature, exceptions);

        if (HTTP_CIRCLE.equals(policyName)) {
            return new HttpAdviceAdapter(api, jsrInlinerAdapter, access, name, descriptor, policy);
        } else if (HTTP_BODY.equals(policyName)) {
            return new HttpBodyAdviceAdapter(api, jsrInlinerAdapter, access, name, descriptor);
        } else if (HTTP_BODY_READ.equals(policyName)) {
            return new HttpBodyReadAdviceAdapter(api, jsrInlinerAdapter, access, className, name, descriptor, policy);
        }
        return jsrInlinerAdapter;
    }

    // 是否native方法
    private boolean isNative(final int access) {
        return (access & Opcodes.ACC_NATIVE) != 0;
    }

}
