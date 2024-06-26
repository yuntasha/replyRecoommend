package capstone.replyRecoommend.marker.service;

import capstone.replyRecoommend.global.exception.BusinessException;
import capstone.replyRecoommend.global.exception.errorcode.CommonErrorCode;
import capstone.replyRecoommend.marker.domain.Marker;
import capstone.replyRecoommend.marker.dto.MarkerRes;
import capstone.replyRecoommend.marker.repository.MarkerRepository;
import capstone.replyRecoommend.marker.util.HospitalUtil;
import capstone.replyRecoommend.marker.web.MapWebClient;
import capstone.replyRecoommend.marker.web.dto.HospitalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerServiceImpl implements MarkerService{
    private final MapWebClient mapWebClient;
    private final MarkerRepository markerRepository;

    @Transactional
    public List<MarkerRes.MarkerPointRes> getMarkerList(){
        List<Marker> nodes = markerRepository.findAll();
        return nodes.stream().map(Marker::toMarkerPoint).toList();
    }

    @Transactional
    public List<MarkerRes.MarkerPointRes> getNearList(String lat, String lng, Marker.Type type){
        List<Marker> near = findNear(lat, lng, type);
        return near.stream().map(Marker::toMarkerPoint).toList();
    }

    private List<Marker> findNear(String lat, String lng, Marker.Type type){
        Double rng = 0.016;
        if (type==null) return markerRepository.findNear(Double.valueOf(lat), Double.valueOf(lng), rng);
        else return markerRepository.findNearAndType(Double.valueOf(lat), Double.valueOf(lng), rng, type);
    }

    @Transactional
    public MarkerRes.MarkerDetailRes getDetails(Long id){
        Marker node = markerRepository.findById(id).orElseThrow(() -> new BusinessException(CommonErrorCode.MARKER_NOT_FOUND));
        return node.toDetail();
    }

    @Transactional
    public void loadData(){
        String list = mapWebClient.getList();
        HospitalDTO[] hospitalDTOS = HospitalUtil.getMapInfo(list);

        List<Marker> markerList = dtoToMapNode(hospitalDTOS, Marker.Type.PHARMACY);
        saveMapNodes(markerList);
    }

    private List<Marker> dtoToMapNode(HospitalDTO[] hospitalDTOS, Marker.Type type){
        List<Marker> markerList = new ArrayList<Marker>();
        for (HospitalDTO hospitalDTO: hospitalDTOS){
            markerList.add(new Marker(hospitalDTO, type));
        };
        return markerList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMapNodes(List<Marker> list){
        markerRepository.saveAll(list);
    }
}
