package com.monglife.discovery.gateway.app.dto.response;

import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.core.vo.passport.PassportDataAppVersionVo;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PassportDataResponseDto {

    private PassportDataAccountVo passportDataAccountVo;

    private PassportDataAppVersionVo passportDataAppVersionVo;
}
