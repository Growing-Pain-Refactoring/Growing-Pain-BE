package cotato.growingpain.converter;

import cotato.growingpain.member.domain.entity.Member;
import cotato.growingpain.member.dto.MemberInfoDto;

public class MemberConverter {

    public static MemberInfoDto toMemberInfoDto(Member member) {
        return new MemberInfoDto(member.getEmail(), member.getName());
    }
}
