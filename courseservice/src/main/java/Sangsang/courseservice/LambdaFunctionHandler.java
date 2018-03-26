public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {
 
 private AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
   .withRegion(Regions.US_EAST_1).build();
 private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
		 .withRegion(Regions.US_EAST_1).build();
 DynamoDB dynamoDB = new DynamoDB(client);
 
 private static String SNS_PREFFIX = "arn:aws:sns:us-east-1:895203570867:";
 
    @Override
    public String handleRequest(DynamodbEvent input, Context context) {
      // Read DDB Recoeds
      context.getLogger().log("Input: " + input);
      
      for(DynamodbStreamRecord record : input.getRecords()) {
       if(record != null) {
        String event = record.getEventName();
        if(!event.equals("INSERT"))
         continue;
        
        String courseId = getCourseId(record);
        String subject = "New announcement in " + courseId;
        StringBuilder outputBody = new StringBuilder();
            outputBody.append("New announcement\n");
            outputBody.append(getContents(record));
            String topicArn = SNS_PREFFIX + courseId;
            sendEmailNotification(topicArn, subject, outputBody.toString());
       }
      }
        return input.toString();
    }
    
    private void sendEmailNotification(final String topicArn, final String subject, final String message) {
      PublishRequest pr = new PublishRequest(topicArn, message, subject);
      SNS_CLIENT.publish(pr);
    }
    
    private String getCourseId(DynamodbStreamRecord record) {
      Map<String, AttributeValue> map = record.getDynamodb().getNewImage();
      String professorId = map.get(“ProfessorId”).getS();   
      Table table = dynamoDB.getTable("Courses");
      QuerySpec spec = new QuerySpec()
    		    .withKeyConditionExpression("professorId = " + professorId)
    		    .withValueMap(new ValueMap()
    		        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1"));
      ItemCollection<QueryOutcome> items = table.query(spec);
      Iterator<Item> iterator = items.iterator();
      Item item = null;
      while (iterator.hasNext()) {
          item = iterator.next();
          System.out.println(item.toJSONPretty());
          return item.toString()
      }
      return "";
    }
    
    private String getContents(DynamodbStreamRecord record) {
  Map<String, AttributeValue> map = record.getDynamodb().getNewImage();
  String contents = map.get(“Contents”).getS();
  return contents;
    }
}