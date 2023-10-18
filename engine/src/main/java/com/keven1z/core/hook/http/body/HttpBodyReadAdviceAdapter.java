package com.keven1z.core.hook.http.body;

import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.hook.http.HttpSpy;
import com.keven1z.core.policy.Policy;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.spy.SimpleIASTSpyManager;

public class HttpBodyReadAdviceAdapter extends AdviceAdapter {
    private static final String READ_BODY_1 = "()I";
    private static final String READ_BODY_2 = "([B)I";
    private static final String READ_BODY_3 = "([BII)I";
    private String desc;

    public HttpBodyReadAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, Policy policy) {
        super(api, mv, access, name, desc);
        this.desc = desc;
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        pushReturnValue(opcode);
        Type type = Type.getType(SimpleIASTSpyManager.class);
        loadThis();
        if (READ_BODY_1.equals(desc)) {
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_1);
        } else if (READ_BODY_2.equals(desc)) {
            loadArg(0);
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_2);
        } else if (READ_BODY_3.equals(desc)) {
            loadArg(0);
            loadArg(1);
            loadArg(2);
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_3);
        }
    }


    private void pushReturnValue(int opcode) {
        if (opcode == RETURN) {
            visitInsn(ACONST_NULL);
        } else if (opcode == ARETURN || opcode == ATHROW) {
            dup();
        } else {
            if (opcode == LRETURN || opcode == DRETURN) {
                dup2();
            } else {
                dup();
            }
            box(Type.getReturnType(this.methodDesc));
        }
    }

    private boolean isThrow(int opcode) {
        return opcode == ATHROW;
    }
}
