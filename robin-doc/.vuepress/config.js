module.exports = {
    title: 'Robin',
    description: '期望成为一个反爬框架',
    themeConfig:
        {
            repo: 'ly-chn/robin',
            docsDir: 'robin-doc',
            docsBranch: 'master',
            editLinks: true,
            locales: {
                '/': {
                    label: "简体中文",
                    selectText: "Languages",
                    editLinkText: "在 GitHub 上编辑此页",
                    lastUpdated: "上次更新",
                }
            },
            sidebar: {
                "/guide/": genGuideSidebar(),
            }
        }
}

function genGuideSidebar() {
    return [
        {
            title: "快速入门",
            collapsable: false,
            children: [
                ["", "介绍"],
                "tmp"
            ]
        }
    ];
}
