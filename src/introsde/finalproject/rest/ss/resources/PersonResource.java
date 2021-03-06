package introsde.finalproject.rest.ss.resources;

//import ehealth.model.MeasureDefinition;
import introsde.finalproject.soap.ws.*;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int idPerson;
    private People people = null;

    public PersonResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
    }

    public PersonResource(UriInfo uriInfo, Request request,int id, People people) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
        this.people = people;
    }
    
    private String errorMessage(){
    	return "{ \n \"error\" : \"Error in LocalDatabaseService\"}";
    }
    
    private String errorMessage(Exception e){
    	return "{ \n \"error\" : \"Error in Storage Services, due to the exception: "+e+"\"}";
    }
    
    private String notFoundMessage(String name, int id){
    	return "{ \n \"error\" : \""+ name +" with id = " + id + " not found\" \n }";
    }

    //********************PERSON********************
    
    /**
     * GET /person/{idPerson}
     * Calls getPerson in LDS
     * Returns a person
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson() {
    	try{
    	System.out.println("getPerson: Reading Person...");
    	Person person = people.getPerson(this.idPerson);
    	if (person == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Person", this.idPerson)).build();
    	else{
    		System.out.println("Person: "+person.getIdPerson()+" "+person.getLastname());
    		return Response.ok(person).build();
    	}
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * PUT /person/{idPerson}
     * Updates person passed as parameter
     * Calls updatePerson() in LDS
     * @param person
     * @return
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON )
	public Response updatePerson(Person person) {
    	try{
    	System.out.println("updatePerson: Updating person with id: "+this.idPerson);
    	person.setIdPerson(this.idPerson);
        int result = people.updatePerson(person);    
        if (result >= 0)
        	return Response.ok(result).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Person", this.idPerson)).build();
        else
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * DELETE /person/{idPerson}
     * Deletes person with id = idPerson
     * Calls deletePerson in LDS
     */
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public Response deletePerson() {
    	try{
    	System.out.println("detetePerson: Deleting person with id: "+ this.idPerson);
        int result = people.deletePerson(this.idPerson);
        if (result == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Person", this.idPerson)).build();
        else
        	return Response.status(Response.Status.NO_CONTENT).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * GET /person/{idPerson}/vitalsings
     * Calls getVitalSigns in LDS
     * This method is used to retrieve vital signs of a person, min and max value
	 * of blood pressure
     * @return
     */
    @GET
    @Path("/vitalsings")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVitalSigns() {
    	try{
    	System.out.println("getVitalSigns: Reading vital signs for idPerson "+ this.idPerson +"...");
    	ListMeasureWrapper result = people.getVitalSigns(this.idPerson);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * GET /person/{idPerson}/currentHealth
     * Calls getCurrentHealth in LDS 
     * @return
     */
    @GET
    @Path("/currentHealth")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCurrentHealth() {
    	try{
    	System.out.println("getCurrentHealth: Reading CurrentHealth for idPerson "+ this.idPerson +"...");
    	ListMeasureWrapper result =  people.getCurrentHealth(this.idPerson);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    //********************TARGET********************
    
    /**
     * GET /person/{idPerson}/target
     * This method is used to get a list of targets for a specified personId
     * Calls getTargetList in LDS
     * @return
     */
    @GET
    @Path("/target")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTargetList() {
    	try{
    	System.out.println("getTargetList: Reading targets for idPerson "+ this.idPerson +"...");
    	ListTargetWrapper result = people.getTargetList(this.idPerson);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * POST /person/{idPerson}/target
     * Calls createTarget in LDS
     * This method is used to add a target for a specific personId
     * @param target Target object
     * @return
     */
    @POST
	@Path("/target")
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
    public Response createTarget(Target target){
    	try{
    	System.out.println("New Target for " + target.getMeasureDefinition().getMeasureName());
        System.out.println("createTarget: Creating new target...");
        int id = this.people.createTarget(target, this.idPerson);
        if(id == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else
        	return Response.status(Response.Status.CREATED).entity(id).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * PUT /person/{idPerson}/target/{targetId}
     * This method is used to update a specified target passed as parameter
     * Calls updateTarget in LDS
     * @param target
     * @param targetId
     * @return
     */
    @PUT
    @Path("/target/{targetId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces( MediaType.APPLICATION_JSON )
	public Response updateTarget(Target target, @PathParam("targetId") int targetId) {
    	try{
    	System.out.println("updateTarget: Updating target with id: "+ targetId);
    	target.setIdTarget(targetId);
        int result = people.updateTarget(target);    
        if (result >= 0)
        	return Response.ok(result).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Target", targetId)).build();
        else
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * DELETE /person/{idPerson}/target/{targetId}
     * Calls deleteTarget in LDS
     * @param targetId
     * @return
     */
    @DELETE
    @Path("/target/{targetId}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteTarget(@PathParam("targetId") int targetId) {
    	try{
    	System.out.println("deteteTarget: Deleting target with id: "+ targetId);
        int result = people.deleteTarget(targetId);
        if (result == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Target", targetId)).build();
        else
        	return Response.status(Response.Status.NO_CONTENT).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * GET /person/{idPerson}/target/{measureDefinitionId}
     * Calls getTarget in LDS
     * This method is used to retrieve information about 
     * target for a specified person and measure
     * Example: check if there are target about weight for the personId 1
     * @param measureDefId
     * @return list of target
     */
    @GET
    @Path("/target/{measureDefinitionId}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTarget(@PathParam("measureDefinitionId") int measureDefId) {
    	try{
    	System.out.println("getTarget: Reading target for idPerson = "+ this.idPerson +
    			" and measureDefId = "+measureDefId+"...");
    	ListTargetWrapper result = people.getTarget(this.idPerson, measureDefId);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    //***********************REMINDER***********************
    
    /**
     * GET /person/{idPerson}/reminder
     * Calls getReminder in LDS
     * @return Reminder
     */
    @GET
    @Path("/reminder")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getReminder() {
    	try{
    	System.out.println("getReminder: Reading reminders for idPerson "+ this.idPerson +"...");
    	ListReminderWrapper result = people.getReminder(this.idPerson);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * POST /person/{idPerson}/reminder
     * Calls createReminder in LDS
     * @param reminder
     * @return
     */
    @POST
	@Path("/reminder")
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
    public Response createReminder(Reminder reminder){
    	try{
    	System.out.println("New Reminder for person" + this.idPerson);
        System.out.println("createReminder: Creating new reminder...");
        int id = this.people.createReminder(reminder, this.idPerson);
        if(id == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else
        	return Response.status(Response.Status.CREATED).entity(id).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * PUT /person/{idPerson}/reminder/{reminderId}
     * Calls updateReminder in LDS
     * @param reminder
     * @param reminderId
     * @return reminderid or error
     */
    @PUT
    @Path("/reminder/{reminderId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces( MediaType.APPLICATION_JSON )
	public Response updateReminder(Reminder reminder, @PathParam("reminderId") int reminderId) {
    	try{
    	System.out.println("updateReminder: Updating reminder with id: "+ reminderId);
    	reminder.setIdReminder(reminderId);
        int result = people.updateReminder(reminder);    
        if (result >= 0)
        	return Response.ok(result).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Reminder", reminderId)).build();
        else
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * DELETE /person/{idPerson}/reminder/{reminderId}
     * Calls deleteReminder in LDS
     * @param reminderId
     * @return 
     */
    @DELETE
    @Path("/reminder/{reminderId}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteReminder(@PathParam("reminderId") int reminderId) {
    	try{
    	System.out.println("deteteReminder: Deleting reminder with id: "+ reminderId);
        int result = people.deleteReminder(reminderId);
        if (result == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Reminder", reminderId)).build();
        else
        	return Response.status(Response.Status.NO_CONTENT).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    //***********************MEASURE***********************
    
    /**
     * GET /person/{idPerson}/measure
     * Calls getMeasure in LDS
     * @return
     */
    @GET
    @Path("/measure")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getMeasure() {
    	try{
    	System.out.println("getMeasure: Reading measures for idPerson "+ this.idPerson +"...");
    	ListMeasureWrapper result = people.getMeasure(this.idPerson);
    	return Response.ok(result).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * POST /person/{idPerson}/measure
     * Calls createMeasure in LDS
     * @param measure
     * @return
     */
    @POST
	@Path("/measure")
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
    public Response createMeasure(Measure measure){
    	try{
    	System.out.println("New Measure for person" + this.idPerson);
        System.out.println("createMeasure: Creating new measure...");
        int id = this.people.createMeasure(measure, this.idPerson);
        if(id == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else
        	return Response.status(Response.Status.CREATED).entity(id).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * PUT /person/{idPerson}/measure/{measureId}
     * Calls updateMeasure in LDS
     * @param measure
     * @param measureId
     * @return
     * @throws ParseException_Exception
     */
    @PUT
    @Path("/measure/{measureId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces( MediaType.APPLICATION_JSON )
	public Response updateMeasure(Measure measure, @PathParam("measureId") int measureId) throws ParseException_Exception {
    	try{
    	System.out.println("updateMeasure: Updating measures with id: "+ measureId);
    	measure.setIdMeasure(measureId);
        int result = people.updateMeasure(measure);    
        if (result >= 0)
        	return Response.ok(result).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Measure", measureId)).build();
        else
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    /**
     * DELETE /person/{idPerson}/measure/{measureId}
     * Calls deleteMeasure in LDS
     * @param measureId
     * @return
     */
    @DELETE
    @Path("/measure/{measureId}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteMeasure(@PathParam("measureId") int measureId) {
    	try{
    	System.out.println("deteteMeasure: Deleting Measure with id: "+ measureId);
        int result = people.deleteMeasure(measureId);
        if (result == -1)
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage()).build();
        else if (result == -2)
        	return Response.status(Response.Status.NOT_FOUND)
    				.entity(notFoundMessage("Measure", measureId)).build();
        else
        	return Response.status(Response.Status.NO_CONTENT).build();
    	}catch(Exception e){
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
}