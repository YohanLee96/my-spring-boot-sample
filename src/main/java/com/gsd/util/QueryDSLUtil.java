package com.gsd.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * QueryDSL 관련 유틸 클래스
 * Created by Yohan lee
 * Created on 2021-02-16.
 **/
public class QueryDSLUtil {
    /**
     * Postgres SQL Function
     * 여러행의 컬럼 값을 하나로 연결한다.
     * @param expression 연결 대상 컬럼
     * @param seperator 연결 시에 사용할 구분 기호
     * @return String TemplateExpression Interface
     * (해당 반환값은 Group By, Order By시 사용불가.)
     */
    public static StringTemplate stringAgg(Expression<?> expression, String seperator) {
        return Expressions.stringTemplate("string_agg({0}, {1})", expression, seperator);
    }

    /**
     * 날짜를 변환해준다.
     * @param expression 변환 대상 컬럼
     * @param regEx 변환할 양식.
     * @return String Path Interface
     * (해당 반환값은 Group By, Order By 시 사용가능.)
     */
    public static StringPath toChar(Expression<?> expression, String regEx) {
        StringTemplate stringTemplate = Expressions.stringTemplate("to_char({0},'{1}')", expression, regEx);
        return Expressions.stringPath(stringTemplate.stringValue().toString());
    }

    /**
     * Spring Pageable Sort 객체를 OrderSpecifier 구현체로 변환해준다.
     * @param sort pageable의 Sort 객체
     * @param clazz 변환해줄 필드가 담긴 Class
     * @return OrderSpecifier Array
     */
    public static OrderSpecifier[] getOrderSpecifier(Sort sort, Class<?> clazz) {
        List<OrderSpecifier> orders = new ArrayList<>();
        sort.get().forEach(order -> {
            if(isValidField(clazz, order)) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                orders.add(new OrderSpecifier(direction, new PathBuilder(clazz, order.getProperty())));
            }
        });

        return orders.toArray(new OrderSpecifier[0]);
    }

    private static boolean isValidField(Class<?> clazz, Sort.Order order) {
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> order.getProperty().equals(field.getName()));
    }
}
