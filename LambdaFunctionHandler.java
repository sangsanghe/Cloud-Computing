public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {
 
 private AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
   .withRegion(Regions.US_WEST_2).build();
 
 private static String SNS_PREFFIX = "arn:aws:sns:us-west-2:806121996369:";
 
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
            outputBody.append(getText(record));
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
      System.out.println(record.toString());
      String courseId = map.get("CourseId").getS();
      return courseId;
    }
    
    private String getText(DynamodbStreamRecord record) {
  Map<String, AttributeValue> map = record.getDynamodb().getNewImage();
  String text = map.get("Text").getS();
  return text;
    }
}