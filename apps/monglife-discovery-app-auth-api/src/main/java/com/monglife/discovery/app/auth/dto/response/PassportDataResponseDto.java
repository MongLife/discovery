package com.monglife.discovery.app.auth.dto.response;

import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.core.vo.passport.PassportDataAppVersionVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PassportDataResponseDto {

    private PassportDataAccountVo passportDataAccountVo;

    private PassportDataAppVersionVo passportDataAppVersionVo;
}
