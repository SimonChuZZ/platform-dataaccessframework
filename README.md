# platform-dataaccessframework
集成携程apollo 配置中心的动态切换数据库，mongodb,redis 配置，并集成mybatis 及redis 缓存

platform-dataaccessframework 是一个基于Springboot 的数据访问类库，集成了阿里巴巴druid 数据源，mybatis,redis,mongodb等，固化服务配置，
每个微服务只需要直接依赖这个基础类库，然后将配置模板中的配置内容拷贝至apollo 配置中心后修改配置参数即可直接在工程里依赖使用，不需要在每个微服
务工程中再去写相同代码实现各种数据源。集成apollo能够实现配置线上动态刷新数据源，不要重启应用等。resources 文件夹中有配置的模板。

