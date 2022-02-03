package register.data.mapperToApi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import register.api.model.User;

import java.util.Date;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(source = "created", target = "created", qualifiedByName = "dateToTimestamp")
    @Mapping(source = "introducer", target = "introducer", qualifiedByName = "introducerToIntroducer")
    User modelToApi(register.data.entity.User dataLayerObj);

    @Named("dateToTimestamp")
    default Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }

        return date.getTime();
    }

    @Named("introducerToIntroducer")
    default String introducerToIntroducer(register.data.entity.User introducer) {
        if (introducer != null) {
            return introducer.getUserName();
        }
        return null;
    }
}