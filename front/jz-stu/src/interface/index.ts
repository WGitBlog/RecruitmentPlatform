export interface Job {
  boosId: number // 招聘者ID
  educationRequirements: string // 教育要求
  id: number // 职位ID
  jobCategory: string // 职位类别
  jobDescription: string[] // 职位描述（数组）
  jobDetails: string // 职位详细信息
  jobTitle: string // 职位名称
  publishTime: String // 发布时间（时间戳）
  requiredTechnology: string // 所需技术
  salaryRange: string // 薪资范围
  weeklyDays: number // 每周工作天数
  workLocation: string // 工作地点
  review:number //职位状态
}
export interface JobDto {
    boosId: number // 招聘者ID
    boosImg: string // 招聘者头像路径
    boosName: string // 招聘者姓名
    companyId: number // 公司ID
    companyImg: string // 公司头像路径
    companyMarket: boolean // 公司是否在市场上
    companyName: string // 公司名称
    companyPosition: string // 招聘者职位
    companyScale: string // 公司规模
    companyType: string // 公司类型
    educationRequirements: string // 教育要求
    id: number // 职位ID
    jobCategory: string // 职位类别
    jobDescription: string[] // 职位描述（数组）
    jobDetails: string // 职位详细信息
    jobTitle: string // 职位名称
    publishTime: number // 发布时间（时间戳）
    requiredTechnology: string // 所需技术
    salaryRange: string // 薪资范围
    weeklyDays: number // 每周工作天数
    workLocation: string // 工作地点
    review:number //职位状态
}
export interface RuleForm {
    id: number//ID
   account: string // 电话
    age: number//年龄
    birthday: string//生日
    degreecategory: string//学位
    email: string//邮箱
    gender: number//性别
    workstatus: number//求职状态
    address: string//地址
    name: string//姓名
    weixinblur: string//微信
    resume: string//简历
    workyears:number//工作年份
    desiredSalary:string;
        desiredCity: string; // 期望城市
      desiredIndustry: string; // 期望行业
      desiredPosition: string; // 期望职位
}
// 定义 UserInfo 接口
export interface UserInfo {
  id?: number; // 应聘者唯一标识符
  account?: string; // 手机号码
  age?: number; // 年龄
  birthday?: string; // 出生日期
  workstatus?: number; // 个人信息是否完整
  degreecategory?: string; // 学历类别
  email?: string; // 电子邮件
  freshgraduate?: number; // 是否为应届生
  gender?: number; // 性别
  address?: string; // 地址
  name?: string; // 姓名
  startworkdate?: number; // 开始工作日期
  tiny?: string; // 头像链接
  weixinblur?: string; // 模糊处理的微信号
  desiredSalary?: string; // 工作经验描述
  workyears?: number; // 工作年限
  resume?: string; // 简历内容
  desiredCity?: string; // 期望城市
  desiredIndustry?: string; // 期望行业
  desiredPosition?: string; // 期望职位
}
//消息
export interface messages {
  msgContent: string // 消息内容
  msgId: number // 消息Id
  msgType: number // 消息类型
  received: number // 是否被接收
  recipientId: number // 接受者的UserId
  senderId: number // 发送者的UserId
  createTime: string // 创建时间
}
export interface candidate {
  id?: number // 应聘者唯一标识符
  account?: string // 手机号码
  age?: number // 年龄
  applicantId?: number //关系Id
  birthday?: string // 出生日期
  complete?: number // 个人信息是否完整
  degreecategory?: string // 学历类别
  email?: string // 电子邮件
  freshgraduate?: number // 是否为应届生
  gender?: number // 性别
  address?: string // 地址
  name?: string // 姓名
  startworkdate?: number // 开始工作日期
  tiny?: string // 头像链接
  weixinblur?: string // 模糊处理的微信号
  desiredSalary?: string // 工作经验描述
  workyears?: number // 工作年限
  resume?: string // 简历内容
  desiredCity?: string // 期望城市
  desiredIndustry?: string // 期望行业
  desiredPosition?: string // 期望职位
}
export interface boos{
  id?: number 
  boosName?:String
  boosEmail?:String
  boosPhone?:String
  companyId?:number
  boosImg?:String
  companyPosition?:String
  applicantCdsId?:number
}
export interface company{
  id?: number 
  companyName?:String
  location?:String
  industry?:String
  establishedYear?:number
  contactNumber?:String
  email?:String
  companyImg?:String
  companyScale?:String
  companyMarket?:number
  companyType?:String
}

export interface report {
  id: number; // 举报记录的唯一标识符
  jobId: number; // 职位的唯一标识符
  jobTitle: string; // 职位名称
  companyId: number; // 公司唯一标识符
  boosName: string; // 发布者姓名
  companyName: string; // 公司名称
  reason: string; // 举报原因
  status: number; // 举报状态（0: 待处理, 1: 已处理, 2: 已驳回）
  createTime: Date; // 举报创建时间
  feedback?: string; // 处理反馈（可选）
  reportDetails: string; // 举报详细说明
  reporterId: number; // 举报用户的唯一标识符
}
export  interface ReportResponse {
  code: number;
  data: any;
  msg: string;
}