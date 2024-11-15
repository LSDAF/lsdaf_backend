package com.lsadf.admin.bdd.config;

import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.models.*;
import com.lsadf.core.responses.GenericResponse;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Stack;

@TestConfiguration
public class LsadfAdminBddConfiguration {

    @Bean
    @Primary
    public BddStackCleaner bddStackCleaner() {
        return new BddStackCleaner();
    }

    @Bean
    public Stack<MimeMessage> mimeMessageStack(BddStackCleaner stackCleaner) {
        Stack<MimeMessage> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Characteristics> characteristicsStack(BddStackCleaner stackCleaner) {
        Stack<Characteristics> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Currency> currencyStack(BddStackCleaner stackCleaner) {
        Stack<Currency> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Stage> stageStack(BddStackCleaner stackCleaner) {
        Stack<Stage> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<GameSave>> gameSaveStack(BddStackCleaner stackCleaner) {
        Stack<List<GameSave>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Inventory> inventoryStack(BddStackCleaner stackCleaner) {
        Stack<Inventory> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<InventoryEntity> inventoryEntityStack(BddStackCleaner stackCleaner) {
        Stack<InventoryEntity> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Item> itemStack(BddStackCleaner stackCleaner) {
        Stack<Item> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<User>> userListStack(BddStackCleaner stackCleaner) {
        Stack<List<User>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<GlobalInfo> globalInfoStack(BddStackCleaner stackCleaner) {
        Stack<GlobalInfo> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<JwtAuthentication> jwtAuthenticationStack(BddStackCleaner stackCleaner) {
        Stack<JwtAuthentication> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Boolean> booleanStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<Boolean>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<UserInfo>> userInfoListStack(BddStackCleaner stackCleaner) {
        Stack<List<UserInfo>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<GameSaveEntity>> gameSaveEntityListStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<List<GameSaveEntity>>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Exception> exceptionStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<Exception>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<GenericResponse<?>> responseStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<GenericResponse<?>>();
        stackCleaner.addStack(stack);
        return stack;
    }

}
