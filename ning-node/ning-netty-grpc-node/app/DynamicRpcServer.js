var PROTO_FILE_PATH = "../proto/Student.proto"
var grpc = require('grpc')
var grpcService = grpc.load(PROTO_FILE_PATH).io.grpc.examples.student;
var server = new grpc.Server();
server.addService(grpcService.StudentService.service,{
    getByName:getByName,
    getByAge:getByAge,
    getByCity:getByCity,
    getByProvince:getByProvince
});
var res = {
    name :"u-res",
    age: 2 ,
    city : "city-res",
    province:"provinec-res"
}
server.bind("localhost:9981",grpc.ServerCredentials.createInsecure());
server.start();
function getByName(call,callback){
    console.log("[server],request msg is :" + call);
    callback(null,res);
}
function getByAge(call,callback){

}
function getByCity(call,callback){

}
function getByProvince(call,callback){

}
