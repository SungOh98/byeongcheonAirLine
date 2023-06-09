package com.koreatech.byeongcheonairlineapi.service;


import com.koreatech.byeongcheonairlineapi.dto.Model.ResponseFlight;
import com.koreatech.byeongcheonairlineapi.dto.Model.RequestFlight;
import com.koreatech.byeongcheonairlineapi.dto.SeatPriceDto;

import java.util.List;

public interface FlightService {

    List<ResponseFlight> goTrip(RequestFlight requestFlight);

    List<ResponseFlight> comeHome(RequestFlight requestFlight);

//    SeatPriceDto findSeatPriceByFlight(int flightId);
}
