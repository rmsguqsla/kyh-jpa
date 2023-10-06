package jpabook.jpashop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JPAQueryFactory query;

    private final EntityManager em;

    private QOrder order = QOrder.order;
    private QMember member = QMember.member;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {


        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(
                        statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName())
                )
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        return !StringUtils.hasText(memberName) ? null : member.name.contains(memberName);
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        return orderStatus == null ? null : order.status.eq(orderStatus);
    }


}
