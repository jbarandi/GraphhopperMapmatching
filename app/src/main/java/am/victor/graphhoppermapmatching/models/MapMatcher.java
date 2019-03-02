package am.victor.graphhoppermapmatching.models;

/**
 * Created by victor on 2/16/19.
 */

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.matching.EdgeMatch;
import com.graphhopper.matching.MapMatching;
import com.graphhopper.matching.MatchResult;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.profiles.BooleanEncodedValue;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.IntsRef;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.shapes.GHPoint;

import java.util.List;

public class MapMatcher {

    private GraphHopper graphHopper;
    private MapMatching mapMatching;

    // import OpenStreetMap data
    public MapMatcher() {

        super();
        // TODO Auto-generated constructor stub

        graphHopper = new GraphHopper();
//        graphHopper.setOSMFile("./map-data/leipzig_germany.osm.pbf");
//        graphHopper.setGraphHopperLocation("./target/mapmatchingtest");
//        FlagEncoder encoder = new CarFlagEncoder();
//        graphHopper.setEncodingManager(EncodingManager.create(encoder));
        graphHopper.setCHEnable(false);
        graphHopper.importOrLoad();

        // create MapMatching object, can and should be shared accross threads

        AlgorithmOptions algorithmOptions = AlgorithmOptions.start().build();
        mapMatching = new MapMatching(graphHopper, algorithmOptions);

    }

    private List<GHPoint> performMatch( List<GHPoint> ghPointList ) {

        List<GHPoint> matchedGHPointList = null;

        List<GPXEntry> inputGPXEntries = createGPXEntryList(ghPointList);

        MatchResult matchResult = mapMatching.doWork(inputGPXEntries);

        // return GraphHopper edges with all associated GPX entries
        List<EdgeMatch> matches = matchResult.getEdgeMatches();
        // now do something with the edges like storing the edgeIds or doing fetchWayGeometry etc
        double latitude = matches.get(0).getGpxExtensions().get(0).getEntry().lat;  /* getEdgeState();*/

        return matchedGHPointList;

    }


    private List<GPXEntry> createGPXEntryList(List<GHPoint> ghPointList ) {
        GHRequest ghRequest = new GHRequest(ghPointList);
        List<Path> paths = graphHopper.calcPaths(ghRequest.setWeighting("fastest"), new GHResponse());
        Translation tr = graphHopper.getTranslationMap().get("en");
        InstructionList instr = paths.get(0).calcInstructions(new BooleanEncodedValue() {
            @Override
            public void setBool(boolean b, IntsRef intsRef, boolean b1) {

            }

            @Override
            public boolean getBool(boolean b, IntsRef intsRef) {
                return false;
            }

            @Override
            public int init(InitializerConfig initializerConfig) {
                return 0;
            }

            @Override
            public String getName() {
                return null;
            }
        }, tr);
        return instr.createGPXList();
    }

}