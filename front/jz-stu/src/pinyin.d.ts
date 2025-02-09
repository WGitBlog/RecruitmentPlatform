declare module 'pinyin' {
    interface PinyinOptions {
        style?: number; // 根据需要添加其他选项
        heteronym?: boolean;
    }

    function pinyin(input: string, options?: PinyinOptions): string[][];

    export default pinyin;
}
