package com.mine.base.commom.designpatten.proxy;

/**
 * project Ants
 *
 * @author hewei
 * @version 15/12/23
 */
public class DemoUser implements ILawsuit{
    @Override
    public void submit() {
        System.out.println("DemoUser -> submit");
    }

    @Override
    public void burden() {
        System.out.println("DemoUser -> burden");
    }

    @Override
    public void finish() {
        System.out.println("DemoUser -> finish");
    }
}
