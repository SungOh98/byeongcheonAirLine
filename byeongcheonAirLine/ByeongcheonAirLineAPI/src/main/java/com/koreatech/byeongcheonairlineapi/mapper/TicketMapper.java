package com.koreatech.byeongcheonairlineapi.mapper;

import com.koreatech.byeongcheonairlineapi.dto.domain.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketMapper {
    @Select("""
            SELECT *
            FROM ticket
            """)
    List<Ticket> findAll();

    @Select("""
            SELECT *
            FROM ticket
            WHERE id = #{id}
            """)
    Ticket findById(int id);

    @Select("""
            SELECT *
            FROM ticket t
            JOIN member m
            ON m.id = t.memberId
            WHERE m.account = #{account}
            """)
    Ticket findByAccount(String account);

    @Insert("""
            INSERT INTO ticket (state, payment, cardNo, memberId, flightId, customerId, boardId, seatId)
            VALUES ('reserved', #{payment}, #{cardNo}, #{memberId}, #{flightId}, #{customerId}, #{boardId}, #{seatId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Ticket ticket);
}
