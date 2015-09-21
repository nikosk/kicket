package gr.dsigned.eservices.kicket;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class PageTransformer implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(PageTransformer.class);

    private static final ConcurrentHashMap<String, Class<? extends WebPage>> CACHE =
        new ConcurrentHashMap<String, Class<? extends WebPage>>();

    public static Class<? extends WebPage> extend(Class<? extends WebPage> clazz) {
        if (CACHE.keySet().contains(clazz.getName())) {
            logger.debug("Page {} found in cache. Returning.", clazz);
            return CACHE.get(clazz.getName());
        }
        logger.debug("Page {} not found in cache. Processing....", clazz);
        String superInternalClassName = Type.getInternalName(clazz);
        String subClassName = clazz.getName().concat("Impl");
        String subClassInternalName = clazz.getName()
                                           .concat("Impl")
                                           .replace(".", "/");
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        MethodVisitor mv;
        FieldVisitor fv;
        String typeDescriptor = "L" + subClassInternalName + ";";
        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, subClassInternalName, null, superInternalClassName,
                 new String[]{Type.getInternalName(EventBusProvider.class)});

        //Create eventBus field in subclass
        {
            fv = cw.visitField(ACC_PRIVATE, "eventBus", "Lgr/dsigned/eservices/kicket/events/EventBus;", null, null);
            fv.visitEnd();
        }
        // Override constructors to call init() after super();
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superInternalClassName, "<init>", "()V");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, subClassInternalName, "init", "()V");
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", typeDescriptor, null, l0, l3, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/apache/wicket/PageParameters;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, superInternalClassName, "<init>", "(Lorg/apache/wicket/PageParameters;)V");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, subClassInternalName, "init", "()V");
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", typeDescriptor, null, l0, l3, 0);
            mv.visitLocalVariable("parameters", "Lorg/apache/wicket/PageParameters;", null, l0, l3, 1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "init", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, Type.getInternalName(ViewParser.class));
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(ViewParser.class), "<init>", "()V");
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(Type.getType(typeDescriptor));
            mv.visitLdcInsn(clazz.getSimpleName() + ".html");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;");
            mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ViewParser.class), "scan", "(Ljava/io/InputStream;)V");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(ViewParser.class), "getComponentRoots", "()Ljava/util/List;");
            mv.visitInsn(ICONST_0);
            mv.visitTypeInsn(ANEWARRAY, Type.getInternalName(Component.class));
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, "[Lorg/apache/wicket/Component;");
            mv.visitMethodInsn(INVOKEVIRTUAL, superInternalClassName, "add", "([Lorg/apache/wicket/Component;)Lorg/apache/wicket/MarkupContainer;");
            mv.visitInsn(POP);
            mv.visitTypeInsn(NEW, Type.getInternalName(ComponentInjector.class));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(ComponentInjector.class), "<init>", "(Lorg/apache/wicket/markup/html/WebPage;)V");
            mv.visitInsn(RETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", typeDescriptor, null, l0, l4, 0);
            mv.visitLocalVariable("viewParser", Type.getDescriptor(ViewParser.class), null, l0, l4, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        // Implement provide method from EvenBusProvider interface
        {
            mv = cw.visitMethod(ACC_PUBLIC, "provide", "()Lgr/dsigned/eservices/kicket/events/EventBus;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(16, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, subClassInternalName, "eventBus", "Lgr/dsigned/eservices/kicket/events/EventBus;");
            Label l1 = new Label();
            mv.visitJumpInsn(IFNONNULL, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(17, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, "gr/dsigned/eservices/kicket/events/EventBus");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "gr/dsigned/eservices/kicket/events/EventBus", "<init>", "()V");
            mv.visitFieldInsn(PUTFIELD, subClassInternalName, "eventBus", "Lgr/dsigned/eservices/kicket/events/EventBus;");
            mv.visitLabel(l1);
            mv.visitLineNumber(19, l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, subClassInternalName, "eventBus", "Lgr/dsigned/eservices/kicket/events/EventBus;");
            mv.visitInsn(ARETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", typeDescriptor, null, l0, l3, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        final Class<? extends WebPage> extendedClass = loadClass(subClassName, cw.toByteArray());
        logger.debug("Extended and loaded {}.", extendedClass.getName());
        CACHE.put(clazz.getName(), extendedClass);
        return extendedClass;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends WebPage> loadClass(String className, byte[] b) {
        Class<?> clazz = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                cls.getDeclaredMethod("defineClass", new Class[]{
                    String.class, byte[].class, int.class, int.class});
            // protected method invocaton
            method.setAccessible(true);
            try {
                Object[] args = new Object[]{className, b, new Integer(0),
                                             new Integer(b.length)};
                logger.debug("Loading {} definition with {}.", className, loader);
                clazz = (Class<?>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (Class<? extends WebPage>) clazz;
    }
}
