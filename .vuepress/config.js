module.exports = {
    dest: 'docs',
    base: '/docs-robin/',
    title: 'Robin',
    description: '期望成为一个反爬框架',
    themeConfig: {
        repo: 'ly-chn/robin',
        docsDir: 'robin-doc',
        docsBranch: 'master',
        editLinks: true,
        locales: {
            '/': {
                label: '简体中文',
                selectText: 'Languages',
                editLinkText: '在 GitHub 上编辑此页',
                lastUpdated: '上次更新',
                nav: [{
                    text: "指南",
                    link: "/guide/"
                }, {
                    text: "配置",
                    link: "/configuration/"
                }, {
                    text: "更新日志",
                    link: ""
                },]
            }
        },
        sidebar: {
            '/guide/': genGuideSidebar(),
            '/configuration/': genConfigSidebar()
        }
    },
    markdown: {
        plugins: ['task-lists'],
        lineNumbers: true
    }
}

function genGuideSidebar() {
    return [
        {
            title: '快速入门',
            collapsable: false,
            children: [
                ['', '介绍'],
                ['quick_start', '快速开始'],
                ['built-in_strategy', '内置策略'],
                ['about_spider', '爬虫的一点介绍'],
            ]
        }
    ];
}

function genConfigSidebar() {
    return [
        {
            title: '配置',
            collapsable: false,
            children: ['']
        }
    ];
}
