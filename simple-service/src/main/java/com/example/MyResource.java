package com.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.HashMap;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/ws")
public class MyResource {
    public static final String ROOT="/tmp";
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleCampaign(@Context HttpServletRequest _request, @PathParam("id") String id) throws IOException {
        if (id == null)
            throw new RuntimeException("id is required");
        System.out.println("receive request from FE of IO:"+id);

        String obj = transformToJson(ROOT+"/"+id+".txt");
        if(obj != null)
            return Response.status(Response.Status.ACCEPTED).entity(obj).type(MediaType.APPLICATION_JSON).build();
        else return Response.status(Response.Status.NOT_FOUND).build();
    }

    private String transformToJson(String filename) throws IOException {
        System.out.println("handle file:" + filename);
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        ResponseJson responseJson = new ResponseJson();
        while((line=br.readLine()) != null) {
            String[] lines = line.split("\\|");
            assert(lines.length == 5);
            String key = lines[0];    //income or education.....
            String vcType = lines[1]; //view or click
            String maType = lines[2]; //mobile or all
            String keyType = lines[3]; //10~20$/year
            String count = lines[4];
            if(vcType.equals("")) {vcType="Unknown";}
            if(maType.equals("")) {maType="Unknown";}
            if(keyType.equals("")) {keyType="Unknown";}
            System.out.println("key is "+key+";vcType="+vcType+";maType="+maType+";keyType="+keyType+";count="+count);
            responseJson.add(key, vcType+"_"+maType, count, keyType);
        }
        return responseJson.toJson();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getList(@Context HttpServletRequest _request) throws IOException {
        System.out.println("receive request from FE to get IO list");
        File dir = new File(ROOT);
        String[] files = dir.list(new FilenameFilter() {
            private String regex = "txt";
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(regex);
            }
        });
        HashMap<String, String> idmapping = new HashMap<String, String>();
        idmapping.put("614121051","Discover Financial Services LLC");
        idmapping.put("614980051","T Rowe Price Investment Services Inc");
        idmapping.put("615295551","General Mills");
        JSONObject js = new JSONObject();
        for (int i = 0; i < files.length; i++) {
            String id = files[i].substring(0, files[i].indexOf("."));
            System.out.println("id is:"+id);
            if(idmapping.get(id) != null) {
                js.put(id, idmapping.get(id));
            }
        }
        return Response.status(Response.Status.ACCEPTED).entity(js.toJSONString()).type(MediaType.APPLICATION_JSON).build();
    }
}
