/**
* 定义包名
**/
namespace java com.duowan.thrift.service

/**
* 服务接口, 仅测试用
**/
service HiService {

    /**
	* 简单的连接测试
	*/
	void ping();

	/**
	* @param name 名称
	**/
	string sayHi(1:string name);
}