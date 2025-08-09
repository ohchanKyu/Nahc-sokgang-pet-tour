package kr.ac.dankook.SokGangPetTour.service.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class TourDataSyncComponent {

    private final TourContentRepository tourContentRepository;

    @Transactional
    public <Req,E> void replaceChildren(
            String contentId,
            List<Req> reqs,
            BiFunction<Req, TourContent, E> mapper,
            Consumer<TourContent> bulkDeleteByContent,
            Consumer<List<E>> bulkSaveAll,
            String successMsg){
        TourContent content = tourContentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 장소 데이터가 존재하지 않습니다."));

        bulkDeleteByContent.accept(content);
        List<E> entities = reqs.stream().map(x -> mapper.apply(x, content))
                .toList();
        bulkSaveAll.accept(entities);
        log.info("{} contentId={}", successMsg, contentId);
    }
}
