var PROTO_FILE_PATH = "../proto/Student.proto"
var grpc = require('grpc')
var grpcService = grpc.load(PROTO_FILE_PATH).io.grpc.examples.student;
console.log(grpcService)
var client = new grpcService.StudentService('localhost:9981',grpc.credentials.createInsecure());
var req = {
    name :"u1",
    age: 2 ,
    city : "beijing",
    province:"beijing"
}
client.getByName(req,function (error,resData) {
    console.log(resData);
    client.close();
})



