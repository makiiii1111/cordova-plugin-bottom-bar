var exec = require('cordova/exec');
var BottomBar = function(){}
BottomBar.prototype.hideBottomBar = function(){
  exec({},{},'BottomBar','hideBottomBar',[])
}
BottomBar.prototype.showBottomBar = function(){
  exec({},{},'BottomBar','showBottomBar',[])
}
BottomBar.prototype.setUrl = function(url){
  exec({},{},'BottomBar','setUrl',[url])
}
module.exports = new BottomBar();

