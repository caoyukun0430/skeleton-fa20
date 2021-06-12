package bearmaps.proj2d.server.handler.impl;

import bearmaps.proj2d.AugmentedStreetMapGraph;
import bearmaps.proj2d.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2d.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2d.utils.Constants.SEMANTIC_STREET_GRAPH;
import static bearmaps.proj2d.utils.Constants.ROUTE_LIST;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    private static final double ROOT_ULLAT = Constants.ROOT_ULLAT;
    private static final double ROOT_ULLON = Constants.ROOT_ULLON;
    private static final double ROOT_LRLAT = Constants.ROOT_LRLAT;
    private static final double ROOT_LRLON = Constants.ROOT_LRLON;
    public static final int TILE_SIZE = Constants.TILE_SIZE;
    private static final double ROOT_LONDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
    private static final double ROOT_LATDPP = (ROOT_ULLAT - ROOT_LRLAT) / TILE_SIZE;

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams,
                                              Response response) {
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        // Keep in mind:
        // LAT: WEI; LON: JING
        // ROOT_ULLAT > ROOT_LRLAT
        // ROOT_LRLON > ROOT_ULLON
        double queryULlon = requestParams.get("ullon");
        double queryULlat = requestParams.get("ullat");
        double queryLRlon = requestParams.get("lrlon");
        double queryLRlat = requestParams.get("lrlat");
        double queryWidth = requestParams.get("w");
        double queryHeight = requestParams.get("h");
        double queryLonDPP = (queryLRlon - queryULlon) / queryWidth;

        // Handle Corner Cases:
        // Case 1: No Coverage
        // Invalid input OR query box for a location that is completely outside of
        // the root longitude/latitudes.
        // Return query: fails in the case
        if ((queryLRlat >= queryULlat || queryULlon >= queryLRlon)
                || (queryLRlon < ROOT_ULLON || queryLRlat > ROOT_ULLAT
                || queryULlon > ROOT_LRLON || queryULlat < ROOT_LRLAT)) {
            return queryFail();
        }
        // For depth calculation, use the query box lon/lat, even if it exceeds ROOT_X
        int depth = getDepth(queryLonDPP);

        // Case 2: Partial Coverage(we use Math.min()/max() to cover it)
//        * If the user goes to the edge of the map beyond where data is available.
//        * If the query box is so zoomed out that it includes the entire dataset.
//        In these cases, simply return what data you do have available.
        double validULlon = Math.max(queryULlon, ROOT_ULLON);
        double validULlat = Math.min(queryULlat, ROOT_ULLAT);
        double validLRlon = Math.min(queryLRlon, ROOT_LRLON);
        double validLRlat = Math.max(queryLRlat, ROOT_LRLAT);
        int ULXindex = getXindex(validULlon, depth);
        int LRXindex = getXindex(validLRlon, depth);
        int ULYindex = getYindex(validULlat, depth);
        int LRYindex = getYindex(validLRlat, depth);

        // Calculate raster_ul_lon, raster_ul_lat, raster_lr_lon, raster_lr_lat
        double rasterLonDPP = ROOT_LONDPP / Math.pow(2, depth);
        double rasterLatDPP = ROOT_LATDPP / Math.pow(2, depth);
        // DPP is distance per pixel, so remember to * TILE_SIZE !!!
        double raster_ul_lon = ROOT_ULLON + TILE_SIZE * rasterLonDPP * ULXindex;
        double raster_ul_lat = ROOT_ULLAT - TILE_SIZE * rasterLatDPP * ULYindex;
        double raster_lr_lon = ROOT_ULLON + TILE_SIZE * rasterLonDPP * (LRXindex + 1);
        double raster_lr_lat = ROOT_ULLAT - TILE_SIZE * rasterLatDPP * (LRYindex + 1);

        // Calculate RenderGrid
        String[][] render_grid = getRenderGrid(depth, ULXindex, LRXindex, ULYindex, LRYindex);

        // Form return map
        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }

    private int getDepth(double queryLonDPP) {
        // Calculate the log2 of queryLonDPP wrt. root LonDPP and ceil it
        double ratio = ROOT_LONDPP / queryLonDPP;
        int depth =  (int) Math.ceil(Math.log(ratio) / Math.log(2));
        if (depth > 7) {
            depth = 7;
        }
        return depth;
    }

    // Calculate the lon index of UL and LR points
    private int getXindex(double lon, int depth) {
        // one edge cases since ceil(0) = 0
        if (lon == ROOT_ULLON) {
            return 0;
        } else {
            double rasterLonDPP = ROOT_LONDPP / Math.pow(2, depth);
            double numOfPixels = (lon - ROOT_ULLON) / rasterLonDPP;
            // Since Xindex starts from 0 left -> right, we ceil and minus 1
            return (int) (Math.ceil(numOfPixels / TILE_SIZE) - 1);
        }
    }

    // Calculate the lat index of UL and LR points
    private int getYindex(double lat, int depth) {
        // one edge cases since ceil(0) = 0
        if (lat == ROOT_LRLAT) {
            return (int) (Math.pow(2, depth) - 1);
        } else {
            double rasterLatDPP = ROOT_LATDPP / Math.pow(2, depth);
            double numOfYPixels = (lat - ROOT_LRLAT) / rasterLatDPP;
            // the Y index is ordered from top to bottom, unlike Xindex.
            int Yindex = (int) (Math.pow(2, depth) - Math.ceil(numOfYPixels / TILE_SIZE));
            return Yindex;
        }
    }

    public String[][] getRenderGrid(int depth, int ULXindex, int LRXindex,
                                    int ULYindex, int LRYindex) {
        // Grid has Y rows and X columns
        int rows = LRYindex - ULYindex + 1;
        int columns = LRXindex - ULXindex + 1;
        String[][] renderGrid = new String[rows][columns];
        // string element in format d1_x0_y0.png
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                renderGrid[i][j] = String.format("d%d_x%d_y%d.png", depth,
                        ULXindex + j, ULYindex + i);
            }
        }
        return renderGrid;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
